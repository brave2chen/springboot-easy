package com.github.brave2chen.springbooteasy.config.statemachine;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.OnStateChanged;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.EnumSet;
import java.util.Optional;

/**
 * 状态及样例
 *
 * @author chenqy28
 * @date 2020-10-10
 */
@Configuration
@EnableStateMachine(name = "demoStateMachine")
public class DemoStateMachineConfig extends EnumStateMachineConfigurerAdapter<DemoStateMachineConfig.States, DemoStateMachineConfig.Events> {

    public enum States {
        INIT, COMMITTED, AUDITED, ARCHIVED
    }

    public enum Events {
        COMMIT, AUDIT, ARCHIVE
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
                .initial(States.INIT)
                .end(States.ARCHIVED)
                .states(EnumSet.allOf(States.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions
                .withExternal()
                .source(States.INIT).target(States.COMMITTED)
                .event(Events.COMMIT)
                .and()
                .withExternal()
                .source(States.COMMITTED).target(States.AUDITED)
                .event(Events.AUDIT)
                .and()
                .withExternal()
                .source(States.AUDITED).target(States.ARCHIVED)
                .event(Events.ARCHIVE)
        ;
    }

    @WithStateMachine(name = "demoStateMachine")
    public class DemoStateMachineBusiness {
        public DemoStateMachineBusiness() {
            System.err.println("init DemoStateMachineBusiness -------------------");
        }

        @OnStateChanged(target = "ARCHIVED")
        public void toState2() {
            System.err.println("@OnStateChanged: ---> ARCHIVED");
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
            demoStateMachine.sendEvent(Events.COMMIT);
            demoStateMachine.sendEvent(Events.AUDIT);
            demoStateMachine.sendEvent(Events.ARCHIVE);
        }
    }
}



