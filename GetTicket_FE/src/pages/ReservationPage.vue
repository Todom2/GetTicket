<template>
  <DefaultLayout>
    <div class="max-w-md mx-auto mt-10">
      <div class="bg-white p-8 rounded-lg shadow">
        <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">
          예매 설정
        </h2>

        <!-- 타이머 (나중에 추가) -->
        <div class="text-center mb-6">
          <div class="text-5xl font-bold text-gray-800">30초</div>
        </div>

        <!-- 예매하기 버튼 (나중에 추가) -->
        <button
          class="w-full bg-gray-400 text-white py-3 rounded-lg mb-6 cursor-not-allowed"
        >
          예매하기
        </button>

        <!-- 좌석 & 인원 (가로 배치) -->
        <div class="flex items-center justify-center gap-4">
          <!-- 좌석 (고정) -->
          <div class="px-4 py-2 border border-gray-300 rounded-lg bg-white">
            좌석 100석
          </div>

          <!-- 인원 (텍스트) -->
          <span class="text-gray-700">인원</span>

          <!-- 인원 선택 (Select) -->
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
import { ref } from "vue";
import DefaultLayout from "../layouts/DefaultLayout.vue";
import Select from "../components/common/Select.vue";

const userCount = ref("1000");

const userCountOptions = [
  { value: "10", label: "10명" },
  { value: "100", label: "100명" },
  { value: "1000", label: "1000명" },
];
</script>
