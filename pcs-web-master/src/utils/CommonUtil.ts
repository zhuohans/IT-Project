import JSEncrypt from 'jsencrypt'

export const encodePassword = (password: string) => {
  const publicKey = import.meta.env.VITE_PUBLIC_KEY
  const encrypt = new JSEncrypt()
  encrypt.setPublicKey(publicKey)
  return encrypt.encrypt(password)
}

/**
 * 将数组按照指定的键值提取器进行分组
 *
 * @param array 待分组的数组，可以是任意类型的数组
 * @param keyExtractor 键值提取器函数，用于指定根据哪个键值进行分组
 * @returns 返回一个对象，其键是通过keyExtractor函数提取的键值，值是具有相同键值的数组项集合
 */
export const groupBy = (array: any, keyExtractor: any) => {
  // 使用reduce函数遍历数组，累加结果为分组后的对象
  return array.reduce((groups: any, item: any) => {
    // 通过键值提取器函数获取当前项的键值
    const key = keyExtractor(item)

    // 如果当前键值在分组对象中不存在，则初始化为空数组
    if (!groups.hasOwnProperty(key)) {
      groups[key] = []
    }

    // 将当前项添加到对应键值的数组中
    groups[key].push(item)

    // 返回累加的结果，最终得到分组后的对象
    return groups
  }, {})
}

/**
 * 计算数组中元素的和
 *
 * 该函数通过指定的值提取器函数从数组的每个元素中提取数值，并计算这些数值的总和
 * 主要用于需要对数组元素进行数值提取并求和的场景
 *
 * @param array 要处理的数组，数组元素类型不限
 * @param valueExtractor 一个函数，用于从数组的每个元素中提取数值
 * @returns 数组中元素的和，类型为number
 */
export const sum = (array: any, valueExtractor: any) => {

  return array.reduce((accumulator: any, item: any) => {

    const value = valueExtractor(item)

    return accumulator + value
  }, 0)
}

export const formatDateTime = (date: any) => {
  let year = date.getFullYear()
  let month = (1 + date.getMonth()).toString().padStart(2, '0')
  let day = date.getDate().toString().padStart(2, '0')
  let hours = date.getHours().toString().padStart(2, '0')
  let minutes = date.getMinutes().toString().padStart(2, '0')
  let seconds = date.getSeconds().toString().padStart(2, '0')

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

export const getYearMonthDay = (date: any) => {
  let year = date.getFullYear()
  let month = (1 + date.getMonth()).toString().padStart(2, '0')
  let day = date.getDate().toString().padStart(2, '0')

  return `${year}-${month}-${day}`
}

export const nowDay = (date: any): number => {
  return date.getDate().toString().padStart(2, '0')
}

export const getStartAndEndOfToday = () => {
  let today = new Date()
  let startOfDay = new Date(
    today.getFullYear(),
    today.getMonth(),
    today.getDate(),
  )
  // 设置时间为00:00:00
  startOfDay.setHours(0, 0, 0, 0)

  let endOfDay = new Date(startOfDay)
  // 设置时间为23:59:59
  endOfDay.setHours(23, 59, 59, 999)

  return {
    startOfDay: formatDateTime(startOfDay),
    endOfDay: formatDateTime(endOfDay),
  }
}

export const getMonthDays = (month: number) => {
  var now = new Date()
  var lastDay = new Date(now.getFullYear(), month + 1, 0)
  return lastDay.getDate()
}

export const getFirstDayOfWeek = (month: number) => {
  var now = new Date()
  var year = now.getFullYear()
  var firstDay = new Date(year, month, 1)
  return firstDay.getDay()
}

export const generateMonthDays = (month: number, dayList: any) => {
  const currentMonthDays = getMonthDays(month)
  let beforeMonthDays = getMonthDays(month - 1)

  const firstDayOfWeek = getFirstDayOfWeek(month)
  if (firstDayOfWeek == 0) {
    for (let i = 1; i < 7; i++) {
      dayList.value.push({ month: month, day: beforeMonthDays })
      beforeMonthDays--
    }
  } else {
    for (let i = 1; i < firstDayOfWeek; i++) {
      dayList.value.push({ month: month, day: beforeMonthDays })
      beforeMonthDays--
    }
  }
  dayList.value.sort((a: any, b: any) => a.day - b.day)
  for (let i = 1; i <= currentMonthDays; i++) {
    dayList.value.push({ month: month + 1, day: i })
  }
  if (dayList.value.length < 42) {
    let afterDays = 42 - dayList.value.length
    for (let i = 1; i <= afterDays; i++) {
      dayList.value.push({ month: month + 1 + 1, day: i })
    }
  }
}

export const deleteCookie = (name) => {
  document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;'
}

export const validateEmail = (email) => {
  var emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/

  return emailRegex.test(email)
}


export const isValidCombination = (str) => {

  const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{6,16}$/


  return regex.test(str)
}
