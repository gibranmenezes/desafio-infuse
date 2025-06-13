export interface AppResponse<T> {
  status: number;
  message: string;
  content: T;
  errors?: AppErrorResponse[];
}

export interface AppErrorResponse {
  code?: string;
  description: string;
  traceId?: string;
}