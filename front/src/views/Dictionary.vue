<template>
  <div class="dictionary">
    <v-data-table
        :headers="headers"
        :items="data"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>
  </div>
</template>

<script>
  export default {
    name: 'Dictionary',

    data() {
      return {
        headers: [
          { text: '枚举类型', align: 'start', sortable: false, value: 'type' },
          { text: '枚举值', value: 'itemValue' },
          { text: '枚举名', value: 'itemName', sortable: false, },
          { text: '描述', value: 'description' },
          { text: '创建时间', value: 'createTime' },
        ],
        data: [],
      }
    },

    created() {
      this.getDictionary();
    },

    methods: {
      async getDictionary() {
        const {code, msg, data: {data, total}} = await this.$axios.get('/dictionary/list');
        if(code !== 200){
          this.data = data;
          console.log(total)
        } else {
          console.error(msg);
        }
      }
    },
  }
</script>
