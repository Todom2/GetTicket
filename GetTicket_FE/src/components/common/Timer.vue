<template>
  <div
    class="text-5xl font-bold text-center"
    :class="seconds === 0 ? 'text-primary' : 'text-gray-800'"
  >
    {{ formattedTime }}
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from "vue";

const props = defineProps({
  seconds: {
    type: Number,
    required: true,
  },
});

const emit = defineEmits(["complete"]);

const remainingSeconds = ref(props.seconds);
let timerInterval = null;

const formattedTime = computed(() => {
  const minutes = Math.floor(remainingSeconds.value / 60);
  const secs = remainingSeconds.value % 60;
  return `${minutes}:${secs.toString().padStart(2, "0")}`;
});

const startTimer = () => {
  timerInterval = setInterval(() => {
    if (remainingSeconds.value > 0) {
      remainingSeconds.value--;
    } else {
      clearInterval(timerInterval);
      emit("complete");
    }
  }, 1000);
};

onMounted(() => {
  startTimer();
});

onUnmounted(() => {
  if (timerInterval) {
    clearInterval(timerInterval);
  }
});
</script>
