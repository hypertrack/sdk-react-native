export type Success<S> = {
  type: 'success';
  value: S;
};

export type Failure<F> = {
  type: 'failure';
  value: F;
};

export type Result<S, F> = Success<S> | Failure<F>;
