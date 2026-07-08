export interface ApiResponse<T> {

  timestamp: string;

  status: number;

  success: boolean;

  data?: T;

  error?: string;
}