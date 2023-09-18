importScripts('https://storage.googleapis.com/workbox-cdn/releases/7.1.1/workbox-sw.js');

const CACHE_NAME = 'my-cache-v1';
const urlsToCache = [
  '/',
  '/src/index.html',
  '/src/favicon.ico',
];


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
