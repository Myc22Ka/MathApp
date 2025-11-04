export type User = {
  id: number;
  login: string;
  firstname: string;
  lastname: string;
  email: string;
  role: string;
  points: number;
  level: number;
  phoneNumber: string;
  address: string;
  dailyTasksCompleted: number;
  dateOfBirth: string;
  gender: string;
  profilePhotoUrl: string;
  exerciseImages: { url: string; name: string }[]; // TODO: Do zmiany potem
  verified: boolean;
};
