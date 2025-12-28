<template>
  <div class="seat-grid">
    <!-- STAGE -->
    <div class="stage-container mb-8">
      <div class="stage">STAGE</div>
    </div>

    <!-- 좌석 그리드 -->
    <div class="seats-container">
      <div v-for="row in rows" :key="row" class="seat-row">
        <!-- 행 레이블 (A, B, C, D, E) -->
        <div class="row-label">{{ row }}</div>

        <!-- 좌석들 -->
        <div class="seats">
          <button
            v-for="num in 20"
            :key="`${row}${num}`"
            @click="handleSeatClick(row, num)"
            :disabled="getSeatStatus(row, num) === 'RESERVED'"
            :class="getSeatClass(row, num)"
            class="seat"
          >
            {{ row }}{{ num }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  seats: {
    type: Array,
    required: true,
  },
  selectedSeat: {
    type: String,
    default: null,
  },
});

const emit = defineEmits(["select"]);

const rows = ["A", "B", "C", "D", "E"];

const getSeatStatus = (row, num) => {
  const seatNumber = `${row}${num}`;
  const seat = props.seats.find((s) => s.seatNumber === seatNumber);
  return seat ? seat.status : "AVAILABLE";
};

const getSeatClass = (row, num) => {
  const seatNumber = `${row}${num}`;
  const status = getSeatStatus(row, num);

  if (props.selectedSeat === seatNumber) {
    return "seat-selected";
  }

  if (status === "RESERVED") {
    return "seat-reserved";
  }

  return "seat-available";
};

const handleSeatClick = (row, num) => {
  const seatNumber = `${row}${num}`;
  const status = getSeatStatus(row, num);

  if (status !== "RESERVED") {
    emit("select", seatNumber);
  }
};
</script>

<style scoped>
.seat-grid {
  width: 100%;
}

.stage-container {
  display: flex;
  justify-content: center;
}

.stage {
  width: 300px;
  padding: 16px;
  border: 2px solid #4b5563;
  border-radius: 8px;
  text-align: center;
  font-weight: bold;
  font-size: 18px;
  color: #374151;
  background-color: #f3f4f6;
}

.seats-container {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.seat-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.row-label {
  width: 24px;
  text-align: center;
  font-weight: bold;
  color: #374151;
}

.seats {
  display: flex;
  gap: 4px;
  flex-wrap: nowrap;
}

.seat {
  width: 32px;
  height: 32px;
  border-radius: 4px;
  font-size: 10px;
  font-weight: 500;
  border: 1px solid #d1d5db;
  cursor: pointer;
  transition: all 0.2s;
}

.seat-available {
  background-color: #4ade80;
  color: white;
  border-color: #22c55e;
}

.seat-available:hover {
  background-color: #22c55e;
  transform: scale(1.05);
}

.seat-reserved {
  background-color: #9ca3af;
  color: white;
  border-color: #6b7280;
  cursor: not-allowed;
}

.seat-selected {
  background-color: white;
  color: #4ade80;
  border: 2px solid #4ade80;
  font-weight: bold;
}
</style>
