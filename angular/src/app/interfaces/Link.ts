export interface Link {
  a: {
    name: string;
    href: string;
    id: string;
    class: string;
    text: string;
  };
  span?: {
    name: string;
    id: string;
    class: string;
    text: string;
  };
}
