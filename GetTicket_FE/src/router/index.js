import { createRouter, createWebHistory } from "vue-router";
import HomePage from "../pages/Homepage.vue";
import ReservationPage from "../pages/ReservationPage.vue";
import QueuePage from "../pages/QueuePage.vue";

const routes = [
  {
    path: "/",
    name: "Home",
    component: HomePage,
  },
  {
    path: "/reservation",
    name: "Reservation",
    component: ReservationPage,
  },
  {
    path: "/queue",
    name: "Queue",
    component: QueuePage,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
