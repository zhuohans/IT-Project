export class UserPo {
  createBy: string
  updateBy: string
  sort: number
  createTime: string
  updateTime: string
  status: number
  age: number
  userId: string
  location: string
  username: string
  nickName?: string | null // It can be a string or null
  password: string
  appId?: string | null // It can be a string or null
  userType?: string | null // It can be a string or null
  expiredTime?: string | null // It can be a string or null
  pwdResetTime?: string | null // It can be a string or null
  gender?: string | null // It can be a string or null
  birthday?: string | null // It can be a string or null
  nativePlace?: string | null // It can be a string or null
  national?: string | null // It can be a string or null
  politicsStatus?: string | null // It can be a string or null
  post?: string | null // It can be a string or null
  postLevel?: string | null // It can be a string or null
  mostEducation?: string | null // It can be a string or null
  graduatedSchool?: string | null // It can be a string or null
  phone?: string | null // It can be a string or null
  email: string
  certificateType?: string | null // It can be a string or null
  certificateNo?: string | null // It can be a string or null
  avatar?: string | null // It can be a string or null
  userConfig?: any
}
