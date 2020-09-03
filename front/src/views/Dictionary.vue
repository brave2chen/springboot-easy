<template>
  <div class="dictionary">
    <v-data-table
        :headers="headers"
        :items="data"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>
    <v-snackbar v-model="snackbar.value" top :timeout="2000" :color="snackbar.color">
      {{ snackbar.text }}
      <template v-slot:action="{ attrs }">
        <v-btn dark text v-bind="attrs" @click="snackbar.value = false">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </template>
    </v-snackbar>
  </div>
</template>

<script>
  export default {
    name: 'Dictionary',

    data() {
      return {
        headers: [
          {text: '枚举类型', align: 'start', sortable: false, value: 'type'},
          {text: '枚举值', value: 'itemValue'},
          {text: '枚举名', value: 'itemName', sortable: false,},
          {text: '描述', value: 'description'},
          {text: '创建时间', value: 'createTime'},
        ],
        data: [],
        total: 0,

        snackbar: {
          text: '',
          color: 'success',
          value: false,
        },
      }
    },

    created() {
      this.getDictionary();
    },

    methods: {
      async getDictionary() {
        const {code, msg, data, page: {totalCount = 0} = {}} = await this.$axios.get('/dictionary/list');

        this.snackbar = {text: msg, value: true, color: code === 0 ? 'success' : 'error'};
        this.data = data;
        this.total = totalCount;
      }
    },
  }
</script>
