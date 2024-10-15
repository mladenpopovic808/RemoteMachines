export interface LoginResponse{
  token:string
  roles:Role[]
}
export interface LoginRequest{
  email:string;
  password:string;
}
export interface User{
  id: number
  lastName: string
  email: string
  name: string
  password:string
  roles: Role[]
}
export interface Cleaner{
  id:number;
  name:string;
  creationDate:Date;
  status:Status;
  active:boolean;
  user:User;
  blocked:boolean;
  cycleCounter:number;
}
export function areCleanersEqual(cleaner1: Cleaner, cleaner2: Cleaner): boolean {
  return (
    cleaner1.id === cleaner2.id &&
    cleaner1.name === cleaner2.name &&
    cleaner1.status === cleaner2.status &&
    cleaner1.active === cleaner2.active &&
    cleaner1.blocked === cleaner2.blocked &&
    cleaner1.cycleCounter === cleaner2.cycleCounter &&
    areUsersEqual(cleaner1.user, cleaner2.user)
  );
}
export function areUsersEqual(user1: User, user2: User): boolean {
  return (
    user1.id === user2.id &&
    user1.lastName === user2.lastName &&
    user1.email === user2.email &&
    user1.name === user2.name &&
    user1.password === user2.password &&
    areRolesEqual(user1.roles, user2.roles)
  );
}
export function areRolesEqual(roles1: Role[], roles2: Role[]): boolean {
  if (roles1.length !== roles2.length) {
    return false;
  }

  // Compare each role in roles1 to a role in roles2
  return roles1.every(role1 =>
    roles2.some(role2 => areIndividualRolesEqual(role1, role2))
  );
}

export function areIndividualRolesEqual(role1: Role, role2: Role): boolean {
  return role1.id === role2.id && role1.name === role2.name;
}

export interface ScheduleCleanerRequest{
  id:number
  date:string;
  time:string;
  action:string;
}


export interface ErrorMessage{
  id:number;
  message:string;
  action:string;
  date:Date;
  cleaner:Cleaner;

}

export interface CreateCleanerRequest{
  name:string,
  email:string
}
export enum Status{
  STOPPED="STOPPED",RUNNING="RUNNING",DISCHARGING="DISCHARGING"
}

export interface UserDto{
  id: number
  name: string
  lastName: string
  email: string
  roles: Role[]
}

//Entiteti
export interface Role{
  id:number,
  name:string
}




