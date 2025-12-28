import { createRouter, createWebHistory } from "vue-router";
import HomePage from "../pages/Homepage.vue";
import ReservationPage from "../pages/ReservationPage.vue";
import QueuePage from "../pages/QueuePage.vue";
import SeatsPage from "../pages/SeatsPage.vue";
import PaymentPage from "../pages/PaymentPage.vue";

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
  {
    path: "/seats",
    name: "Seats",
    component: SeatsPage,
  },
  {
    path: "/payment",
    name: "Payment",
    component: PaymentPage,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
