package com.github.brave2chen.springbooteasy.config.statemachine;

import cn.hutool.core.util.RandomUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.annotation.OnStateMachineError;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.EnumSet;

/**
 * 状态及样例
 *
 * @author brave2chen
 * @date 2020-10-10
 */
@Configuration
@EnableStateMachine(name = "demoStateMachine")
public class DemoStateMachineConfig extends EnumStateMachineConfigurerAdapter<DemoStateMachineConfig.States, DemoStateMachineConfig.Events> {

    public enum States {
        TO_CREATE, TO_MODIFY, SAVED, COMMITTED, AUDITED, ARCHIVED, TRANSITION
    }

    public enum Events {
        SAVE, COMMIT, SAVE_AND_COMMIT, AUDIT, AUDIT_FAIL, ARCHIVE
    }


    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        super.configure(config);
        config.withConfiguration()
                // machineId 不用设置和 name 一样，会导致 WithStateMachine 被多次回调
                .machineId("demoStateMachineId")
                .autoStartup(true)
                .listener(new StateMachineListenerAdapter<States, Events>() {

                    @Override
                    public void stateMachineStarted(StateMachine<States, Events> stateMachine) {
                        super.stateMachineStarted(stateMachine);
                        System.err.println("stateMachineStarted:" + stateMachine.getId());
                    }

                    @Override
                    public void stateMachineStopped(StateMachine<States, Events> stateMachine) {
                        super.stateMachineStopped(stateMachine);
                        System.err.println("stateMachineStopped:" + stateMachine.getId());
                    }

                    @Override
                    public void stateChanged(State<States, Events> from, State<States, Events> to) {
                        System.err.println("stateChanged:" + (from != null ? from.getId() : "null") + " ---> " + to.getId());
                    }
                });
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states
                .withStates()
                .initial(States.TO_CREATE, action())
                .end(States.ARCHIVED)
                .stateDo(States.AUDITED, context -> System.err.println("AUDITED State action"))
                .stateEntry(
                        States.AUDITED,
                        context -> { throw new RuntimeException("AUDITED Entry action");},
                        context -> System.err.println("AUDITED Entry action error:" + context.getException().getMessage())
                )
                .stateExit(States.AUDITED, context -> System.err.println("AUDITED Exit action"))
                .states(EnumSet.allOf(States.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions
                .withExternal()
                .source(States.TO_CREATE).target(States.SAVED).event(Events.SAVE)
                .and()
                .withExternal()
                .source(States.TO_CREATE).target(States.COMMITTED).event(Events.SAVE_AND_COMMIT)
                .and()
                .withExternal()
                .source(States.SAVED).target(States.COMMITTED).event(Events.COMMIT)
                .and()
                .withExternal()
                .source(States.COMMITTED).target(States.AUDITED).event(Events.AUDIT)
                .and()
                .withExternal()
                .source(States.COMMITTED).target(States.TO_MODIFY).event(Events.AUDIT_FAIL)
                .and()
                .withExternal()
                .source(States.TO_MODIFY).target(States.SAVED).event(Events.SAVE)
                .and()
                .withExternal()
                .source(States.TO_MODIFY).target(States.COMMITTED).event(Events.SAVE_AND_COMMIT)
                .and()
                .withExternal()
                .source(States.AUDITED).target(States.ARCHIVED).event(Events.ARCHIVE)
        ;
    }

    public Guard<States, Events> guard(boolean reverse) {
        return new Guard<States, Events>() {
            @Override
            public boolean evaluate(StateContext<States, Events> context) {
                return  (boolean) context.getMessage().getHeaders().get("result") && reverse;
            }
        };
    }

    @Bean
    public Action<States, Events> action() {
        return new Action<States, Events>() {

            @Override
            public void execute(StateContext<States, Events> context) {
                System.err.println("action: " + context.getStateMachine().getState());
            }
        };
    }

    @WithStateMachine(name = "demoStateMachine")
    public class DemoStateMachineBusiness {
        public DemoStateMachineBusiness() {
            System.err.println("init DemoStateMachineBusiness -------------------");
        }

        @OnTransition(target = "ARCHIVED")
        public void toState2(Message<Events> message) {
            System.err.println("@OnTransition: ---> ARCHIVED, message " + message);
        }

        @OnStateMachineError
        public void OnStateMachineError(Exception e) {
            System.err.println(e.getMessage());
        }
    }


    /**
     * DemoStateMachineRunner
     *
     * @author chenqy28
     * @date 2020-10-12
     */
    @Component
    public class DemoStateMachineRunner implements CommandLineRunner {

        @Resource
        private StateMachine<States, Events> demoStateMachine;

        @Override
        public void run(String... args) throws Exception {
            demoStateMachine.sendEvent(Events.ARCHIVE);

            demoStateMachine.sendEvent(Events.SAVE);
            Message<Events> commit = MessageBuilder.withPayload(Events.COMMIT).setHeader("result", RandomUtil.randomBoolean()).build();
            demoStateMachine.sendEvent(commit);
            demoStateMachine.sendEvent(Events.AUDIT_FAIL);
            demoStateMachine.sendEvent(Events.SAVE_AND_COMMIT);
            demoStateMachine.sendEvent(Events.AUDIT_FAIL);
            demoStateMachine.sendEvent(Events.SAVE);
            demoStateMachine.sendEvent(Events.COMMIT);
            demoStateMachine.sendEvent(Events.AUDIT);
            demoStateMachine.sendEvent(Events.ARCHIVE);
        }
    }
}



