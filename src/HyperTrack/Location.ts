export type Location = {
  latitude: number;
  longitude: number;
};

export type SDKLocation = {
  type: 'location';
  value: {
    latitude: number;
    longitude: number;
  };
};
