<template>
  <div v-if="visible" class="modal">
    <div class="modal-overlay" @click="$emit('close')"></div>
    <div class="modal-content">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    visible: {
      type: Boolean,
      required: true,
    },
  },
};
</script>

<style scoped>
.modal {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1000;
  overflow: auto; /* 允许滚动 */
}

.modal-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.4);
  z-index: 999; /* 在modal-content后面 */
}

.modal-content {
  background-color: #f0fdf4; /* 浅绿色背景 */
  border-radius: 10px;
  padding: 30px;
  width: 80vw; /* 模态框宽度为80视口宽度 */
  max-height: 90vh; /* 最大高度为视口的90% */
  overflow-y: auto; /* 如果内容溢出，出现垂直滚动条 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
  position: relative;
  z-index: 1001; /* 在overlay前面 */
}

.modal-content button {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: transparent;
  border: none;
  color: #888;
  font-size: 20px;
  cursor: pointer;
}

.modal-content button:hover {
  color: #333;
}
</style>
