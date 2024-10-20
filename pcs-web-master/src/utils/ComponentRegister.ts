import { createApp } from 'vue'
import alert from '@/components/alert/index.vue'
import loadingComponent from '@/components/loading/index.vue'

export function showAlert(
  text: string,
  type: 'info' | 'success' | 'warning' | 'error' = 'info',
  duration: number = 2000,
) {
  const alertContainer = document.createElement('div')

  alertContainer.style.cssText = `
    position: fixed;
    top: 16px;
    left: 50%;
    transform: translateX(-50%);
    z-index: 2147483647 !important;
    pointer-events: auto;
  `

  document.body.appendChild(alertContainer)

  const alertApp = createApp(alert, { text, type })
  alertApp.mount(alertContainer)

  setTimeout(() => {
    alertApp.unmount()
    document.body.removeChild(alertContainer)
  }, duration)
}

/**
 * 显示加载
 * 返回app实例
 * @param text 要显示的文字
 */
export function loading(text: string) {
  const loadingContainer = document.createElement('div')

  loadingContainer.style.position = 'fixed'
  loadingContainer.style.top = '0'
  loadingContainer.style.left = '50%'
  loadingContainer.style.transform = 'translateX(-50%)'
  loadingContainer.style.zIndex = '1000'
  loadingContainer.id = 'custom_loading'

  document.body.appendChild(loadingContainer)

  const loadingApp = createApp(loadingComponent, { text })
  loadingApp.mount(loadingContainer)
  return loadingApp
}

/**
 * 隐藏加载
 * @param app loading返回的app实例
 */
export function hiddenLoading(app: any) {
  const loading = document.getElementById('custom_loading')
  app.unmount()
  if (loading) {
    document.body.removeChild(loading)
  }
}
