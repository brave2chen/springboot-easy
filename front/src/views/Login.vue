<template>
  <v-app id="inspire">
    <v-main>
      <v-container class="fill-height" fluid>
        <v-row align="center" justify="center">
          <v-col cols="12" sm="8" md="4">
            <v-card class="elevation-12">
              <v-toolbar color="primary" dark flat>
                <v-toolbar-title>Login form</v-toolbar-title>
                <v-spacer></v-spacer>
                <v-tooltip bottom>
                  <template v-slot:activator="{ on }">
                    <v-btn :href="source" icon large target="_blank" v-on="on">
                      <v-icon>mdi-code-tags</v-icon>
                    </v-btn>
                  </template>
                  <span>Source</span>
                </v-tooltip>
              </v-toolbar>
              <v-card-text>
                <ValidationObserver ref="observer">
                  <v-form ref="form" v-model="valid">
                    <ValidationProvider v-slot="{ errors }" name="Username" rules="required">
                      <v-text-field
                          v-model="model.username"
                          label="Username"
                          name="username"
                          prepend-icon="mdi-account"
                          type="text"
                          :error-messages="errors"
                          required
                      ></v-text-field>
                    </ValidationProvider>
                    <ValidationProvider v-slot="{ errors }" name="Password" rules="required">
                      <v-text-field
                          v-model="model.password"
                          id="password"
                          label="Password"
                          name="password"
                          prepend-icon="mdi-lock"
                          type="password"
                          :error-messages="errors"
                          required
                      ></v-text-field>
                    </ValidationProvider>
                  </v-form>
                </ValidationObserver>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn :disabled="!valid" color="primary" @click="login">Login</v-btn>
              </v-card-actions>
            </v-card>
          </v-col>
        </v-row>
        <v-snackbar v-model="snackbar.value" top :timeout="2000" :color="snackbar.color">
          {{ snackbar.text }}
          <template v-slot:action="{ attrs }">
            <v-btn dark text v-bind="attrs" @click="snackbar.value = false">
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </template>
        </v-snackbar>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
  import {mapActions} from 'vuex'
  import {required} from 'vee-validate/dist/rules'
  import {extend, setInteractionMode, ValidationObserver, ValidationProvider} from 'vee-validate'

  setInteractionMode('eager')

  extend('required', {
    ...required,
    message: '{_field_} can not be empty',
  })

  export default {
    name: "Login",

    components: {
      ValidationProvider,
      ValidationObserver,
    },

    data() {
      return {
        valid: false,
        model: {
          username: '',
          password: ''
        },
        source: '',

        snackbar: {
          text: '',
          color: 'success',
          value: false,
        },
      }
    },

    created() {
      this.$vuetify.theme.dark = true
    },

    methods: {
      ...mapActions('user', {toLogin: 'login'}),
      async login() {
        const validate = await this.$refs.observer.validate();
        if (!validate) return;
        const {msg, data} = await this.toLogin(this.model);
        this.snackbar = {text: msg, value: true, color: data ? 'success' : 'error'};

        await this.$router.push('/dictionary')
      }
    }
  }
</script>
