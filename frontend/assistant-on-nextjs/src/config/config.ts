export function getBackendURL() {
  const backendUrl = process.env.NEXT_PUBLIC_BACKEND_URL
  if (!backendUrl) {
    throw new Error("env variable NEXT_PUBLIC_BACKEND_URL is not set")
  }
  return backendUrl
}
