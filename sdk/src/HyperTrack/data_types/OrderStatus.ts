export type OrderStatus =
  | {
      type: 'orderStatusClockIn';
    }
  | {
      type: 'orderStatusClockOut';
    }
  | {
      type: 'orderStatusCustom';
      value: string;
    };
