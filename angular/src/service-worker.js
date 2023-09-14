importScripts('https://storage.googleapis.com/workbox-cdn/releases/7.1.1/workbox-sw.js');
importScripts('workbox-sw.js');
importScripts('workbox-routing.js');
importScripts('workbox-strategies.js');
importScripts('workbox-precaching.js');

const CACHE_NAME = 'my-cache-v1';
const urlsToCache = [
  '/',
  '/index.html',
  '../css/coupons.css',
  './js/script.js',
  './js/index.js',
  './img/icon.png',
];

// workbox.precaching.precacheAndRoute(self.__WB_MANIFEST);
//
// workbox.routing.registerRoute(
//   ({request}) => request.destination === 'script' || request.destination === 'style',
//   new workbox.strategies.CacheFirst()
// );
//
// workbox.routing.registerRoute(
//   ({request}) => request.destination === 'image',
//   new workbox.strategies.CacheFirst()
// );
//
// workbox.routing.registerRoute(
//   ({request}) => request.destination === 'font',
//   new workbox.strategies.CacheFirst()
// );
//
// workbox.routing.registerRoute(
//   ({request}) => request.destination === 'document',
//   new workbox.strategies.NetworkFirst()
// );
//
// workbox.routing.registerRoute(
//   ({url}) => url.pathname.startsWith('/api/'),
//   new workbox.strategies.StaleWhileRevalidate()
// );

self.addEventListener('fetch', event => {
  event.respondWith(
    caches.match(event.request)
      .then(response => {
        return response || fetch(event.request);
      })
  );
});

self.addEventListener('install', event => {
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(cache => {
        console.log('Caching URLs:', urlsToCache);
        return cache.addAll(urlsToCache);
      })
      .catch(error => {
        console.error('Caching failed:', error);
      })
  );
});
