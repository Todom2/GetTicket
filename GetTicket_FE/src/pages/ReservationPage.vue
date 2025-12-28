<template>
  <DefaultLayout>
    <div class="max-w-md mx-auto mt-10">
      <div class="bg-white p-8 rounded-lg shadow">
        <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">
          예매 설정
        </h2>

        <!-- 타이머 (시작 후에만 표시) -->
        <div v-if="isStarted" class="mb-6">
          <Timer :seconds="initialSeconds" @complete="onTimerComplete" />
        </div>

        <!-- 버튼 -->
        <button
          @click="handleButtonClick"
          :disabled="isStarted && !canReserve"
          class="w-full py-3 rounded-lg mb-6 font-semibold transition-colors"
          :class="buttonClass"
        >
          {{ buttonText }}
        </button>

        <!-- 좌석 & 인원 -->
        <div class="flex items-center justify-center gap-4">
          <div class="px-4 py-2 border border-gray-300 rounded-lg bg-white">
            좌석 100석
          </div>
          <span class="text-gray-700">인원</span>
          <Select
            v-model="userCount"
            :options="userCountOptions"
            class="w-40"
          />
        </div>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import DefaultLayout from "../layouts/DefaultLayout.vue";
import Select from "../components/common/Select.vue";
import Timer from "../components/common/Timer.vue";

const router = useRouter();

const userCount = ref("1000");
const isStarted = ref(false);
const canReserve = ref(false);
const initialSeconds = ref(0);

const userCountOptions = [
  { value: "10", label: "10명" },
  { value: "100", label: "100명" },
  { value: "1000", label: "1000명" },
];

const buttonText = computed(() => {
  return isStarted.value ? "예매하기" : "시작하기";
});

const buttonClass = computed(() => {
  if (isStarted.value && !canReserve.value) {
    // 타이머 진행 중 (비활성)
    return "bg-gray-400 text-white cursor-not-allowed";
  }
  // 시작 전 or 0초 (활성)
  return "bg-primary text-white hover:bg-green-600 cursor-pointer";
});

const handleButtonClick = () => {
  if (!isStarted.value) {
    // "시작하기" 클릭
    startReservation();
  } else if (canReserve.value) {
    // "예매하기" 클릭 (0초일 때)
    goToQueue();
  }
};

const startReservation = () => {
  // ⭐ 가짜 API 응답 (나중에 실제 API로 교체)
  const mockResponse = {
    startTime: "2025-12-28T21:30:00",
    remainingSeconds: 30,
  };

  console.log("예매 설정:", {
    userCount: userCount.value,
    response: mockResponse,
  });

  // 타이머 시작
  isStarted.value = true;
  initialSeconds.value = mockResponse.remainingSeconds;
};

const onTimerComplete = () => {
  // 0초 되면 버튼 활성화
  canReserve.value = true;
  console.log("타이머 완료 - 예매 가능");
};

const goToQueue = () => {
  console.log("Queue 페이지로 이동");
  router.push("/queue");
};
</script>
