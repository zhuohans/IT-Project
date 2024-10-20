export const getAssetsResource = (path: string) => {
  return new URL(path, import.meta.url).href
}
