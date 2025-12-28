<template>
  <DefaultLayout>
    <div class="max-w-4xl mx-auto mt-10 px-4">
      <div class="bg-white p-8 rounded-lg shadow">
        <!-- 헤더 -->
        <div class="flex justify-between items-center mb-8">
          <h2 class="text-2xl font-bold text-gray-800">결제하기</h2>
          <div class="text-right">
            <p class="text-sm text-gray-600 mb-1">결제 가능 시간</p>
            <Timer :seconds="300" @complete="onTimerComplete" />
          </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
          <!-- 왼쪽: 주문 상세 -->
          <div>
            <h3 class="text-lg font-bold text-gray-800 mb-4">주문상세</h3>

            <div class="space-y-3">
              <div class="flex justify-between py-2 border-b border-gray-200">
                <span class="text-gray-600">좌석정보</span>
                <span class="font-semibold text-gray-800">B열 4번</span>
              </div>

              <div class="flex justify-between py-2 border-b border-gray-200">
                <span class="text-gray-600">가격정보</span>
                <span class="font-semibold text-gray-800">60000원</span>
              </div>
            </div>
          </div>

          <!-- 오른쪽: 결제 정보 -->
          <div class="bg-gray-50 p-6 rounded-lg">
            <h3 class="text-lg font-bold text-gray-800 mb-4">결제 정보</h3>

            <div class="space-y-3 mb-6">
              <div class="flex justify-between">
                <span class="text-gray-600">티켓금액</span>
                <span class="text-gray-800">60000원</span>
              </div>

              <div class="flex justify-between">
                <span class="text-gray-600">수수료</span>
                <span class="text-gray-800">1000원</span>
              </div>

              <div class="border-t border-gray-300 pt-3 mt-3">
                <div class="flex justify-between items-center">
                  <span class="text-lg font-bold text-gray-800"
                    >최종 결제금액</span
                  >
                  <span class="text-2xl font-bold text-primary">61000원</span>
                </div>
              </div>
            </div>

            <!-- 결제 버튼 -->
            <button
              @click="handlePayment"
              :disabled="isProcessing"
              class="w-full py-4 rounded-lg font-bold text-lg transition-colors"
              :class="
                isProcessing
                  ? 'bg-gray-400 text-white cursor-not-allowed'
                  : 'bg-primary text-white hover:bg-green-600 cursor-pointer'
              "
            >
              {{ isProcessing ? "처리 중..." : "총 61000원 결제하기" }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import DefaultLayout from "../layouts/DefaultLayout.vue";
import Timer from "../components/common/Timer.vue";

const router = useRouter();
const isProcessing = ref(false);

const onTimerComplete = () => {
  console.log("결제 시간 만료");
  alert("결제 시간이 만료되었습니다.");
  // ⭐ 나중에 좌석 해제 API 호출 후 /seats로 이동
  router.push("/seats");
};

const handlePayment = async () => {
  if (isProcessing.value) return;

  isProcessing.value = true;

  try {
    console.log("결제 진행 중...");

    // ⭐ 가짜 결제 처리 (나중에 실제 API로 교체)
    await new Promise((resolve) => setTimeout(resolve, 2000));

    console.log("결제 완료!");
    alert("결제가 완료되었습니다!");

    // ⭐ 나중에 홈으로 또는 완료 페이지로 이동
    router.push("/");
  } catch (error) {
    console.error("결제 실패:", error);
    alert("결제에 실패했습니다.");
  } finally {
    isProcessing.value = false;
  }
};
</script>
