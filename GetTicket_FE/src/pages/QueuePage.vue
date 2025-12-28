<template>
  <DefaultLayout>
    <div class="max-w-2xl mx-auto mt-20">
      <div class="bg-white p-12 rounded-lg shadow-lg">
        <!-- 안내 메시지 -->
        <div class="text-center mb-8">
          <p class="text-xl text-gray-700 mb-2">
            접속 인원이 많아 대기중입니다.
          </p>
          <p class="text-lg text-gray-600">조금만 기다려주세요...</p>
        </div>

        <!-- 대기순서 박스 -->
        <div class="bg-gray-50 border-2 border-gray-300 rounded-lg p-8 mb-8">
          <p class="text-center text-gray-700 text-lg mb-4">나의 대기순서</p>
          <p class="text-center text-6xl font-bold text-gray-800">
            {{ position }}
          </p>
        </div>

        <!-- 하단 안내 -->
        <div class="text-center text-gray-600">
          <p class="mb-1">잠시만 기다려주시면 예매 페이지로 이동합니다.</p>
          <p class="text-sm">
            새로고침 하거나 재접속하시면 대기순서가 초기화됩니다.
          </p>
        </div>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue";
import { useRouter } from "vue-router";
import DefaultLayout from "../layouts/DefaultLayout.vue";

const router = useRouter();
const position = ref(954);
let pollingInterval = null;

// ⭐ 가짜 polling (나중에 실제 API로 교체)
const pollPosition = () => {
  // 임시: 3초마다 10씩 감소
  pollingInterval = setInterval(() => {
    if (position.value > 0) {
      position.value = Math.max(0, position.value - 10);
      console.log("대기순서 갱신:", position.value);

      // 0이 되면 좌석 선택 페이지로 이동
      if (position.value === 0) {
        clearInterval(pollingInterval);
        console.log("입장 가능 - 좌석 선택 페이지로 이동");
        setTimeout(() => {
          router.push("/seats");
        }, 500);
      }
    }
  }, 3000); // 3초마다 polling
};

onMounted(() => {
  console.log("QueuePage 마운트 - polling 시작");
  pollPosition();
});

onUnmounted(() => {
  console.log("QueuePage 언마운트 - polling 중단");
  if (pollingInterval) {
    clearInterval(pollingInterval);
  }
});
</script>
