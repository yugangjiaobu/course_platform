<template>
  <div class="modal-overlay" v-if="visible" @click.self="closeModal">
    <div class="modal-content">
      <!-- 模态框头部 -->
      <header class="modal-header">
        <slot name="header">
          <h3>默认标题</h3>
        </slot>
        <button class="close-button" @click="closeModal">×</button>
      </header>

      <!-- 模态框主体 -->
      <main class="modal-body">
        <slot name="body">默认内容</slot>
      </main>

      <!-- 模态框底部 -->
      <footer class="modal-footer">
        <slot name="footer">
          <button class="default-button" @click="closeModal">关闭</button>
        </slot>
      </footer>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Modal',
  props: {
    visible: {
      type: Boolean,
      required: true,
    },
  },
  emits: ['close'],
  methods: {
    closeModal() {
      this.$emit('close');
    },
  },
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: #fff;
  border-radius: 8px;
  max-width: 500px;
  width: 90%;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.modal-header {
  padding: 16px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-body {
  padding: 16px;
}

.modal-footer {
  padding: 16px;
  border-top: 1px solid #eee;
  text-align: right;
}

.close-button {
  background: transparent;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
}

.default-button {
  padding: 8px 16px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.default-button:hover {
  background: #0056b3;
}
</style>
