import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
    children: [
      {
        path: "/dictionary",
        name: "Dictionary",
        component: () => import("../views/Dictionary.vue")
      },
    ]
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("@/views/Login.vue")
  },
];

const router = new VueRouter({
  routes
});

export default router;
