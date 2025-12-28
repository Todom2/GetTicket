<template>
  <DefaultLayout>
    <div class="max-w-7xl mx-auto mt-10 px-4">
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <!-- 좌석 그리드 (왼쪽) -->
        <div class="lg:col-span-2 bg-white p-6 rounded-lg shadow">
          <SeatGrid
            :seats="seats"
            :selectedSeat="selectedSeatNumber"
            @select="handleSeatSelect"
          />
        </div>

        <!-- 선택 정보 (오른쪽) -->
        <div class="bg-white p-6 rounded-lg shadow">
          <h3 class="text-xl font-bold text-gray-800 mb-4">선택 좌석</h3>

          <div v-if="selectedSeatNumber" class="mb-6">
            <div class="bg-gray-50 p-4 rounded-lg mb-2">
              <p class="text-gray-600 text-sm">일반석</p>
              <p class="text-2xl font-bold text-gray-800">
                {{ selectedSeatRow }}열 {{ selectedSeatNum }}번
              </p>
            </div>
            <div class="bg-gray-50 p-4 rounded-lg">
              <p class="text-gray-600 text-sm">가격</p>
              <p class="text-2xl font-bold text-gray-800">60000원</p>
            </div>
          </div>

          <div v-else class="text-center text-gray-400 py-8">
            좌석을 선택해주세요
          </div>

          <button
            @click="handleConfirm"
            :disabled="!selectedSeatNumber"
            class="w-full py-3 rounded-lg font-semibold transition-colors"
            :class="
              selectedSeatNumber
                ? 'bg-primary text-white hover:bg-green-600 cursor-pointer'
                : 'bg-gray-400 text-white cursor-not-allowed'
            "
          >
            선택 완료
          </button>
        </div>
      </div>
    </div>
  </DefaultLayout>
</template>

<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import DefaultLayout from "../layouts/DefaultLayout.vue";
import SeatGrid from "../components/seat/SeatGrid.vue";

const router = useRouter();
const selectedSeatNumber = ref(null);

// ⭐ 가짜 좌석 데이터 (나중에 API로 교체)
const seats = ref([
  // AVAILABLE: 30개 (초록색)
  // RESERVED: 70개 (회색)
  ...generateSeats(),
]);

function generateSeats() {
  const rows = ["A", "B", "C", "D", "E"];
  const allSeats = [];

  for (const row of rows) {
    for (let num = 1; num <= 20; num++) {
      allSeats.push({
        seatNumber: `${row}${num}`,
        status: "AVAILABLE", // 일단 전부 AVAILABLE
      });
    }
  }

  // 랜덤으로 70개를 RESERVED로 변경
  const shuffled = [...allSeats].sort(() => Math.random() - 0.5);
  for (let i = 0; i < 70; i++) {
    shuffled[i].status = "RESERVED";
  }

  return allSeats;
}

const selectedSeatRow = computed(() => {
  if (!selectedSeatNumber.value) return "";
  return selectedSeatNumber.value.charAt(0);
});

const selectedSeatNum = computed(() => {
  if (!selectedSeatNumber.value) return "";
  return selectedSeatNumber.value.slice(1);
});

const handleSeatSelect = (seatNumber) => {
  // 같은 좌석 클릭 시 선택 해제
  if (selectedSeatNumber.value === seatNumber) {
    selectedSeatNumber.value = null;
  } else {
    selectedSeatNumber.value = seatNumber;
  }
  console.log("좌석 선택:", seatNumber);
};

const handleConfirm = () => {
  if (!selectedSeatNumber.value) return;

  console.log("선택 완료:", selectedSeatNumber.value);

  // ⭐ 나중에 POST /seat API 호출
  // 성공 시 /payment로 이동
  router.push("/payment");
};
</script>
