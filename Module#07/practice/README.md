## Task Test

### Init
mkdir practice
cd practice
npm init

### Install dev Dependencies
npm install karma karma-chrome-launcher karma-browserify karma-coverage karma-spec-reporter --save-dev
npm install jasmine-core jasmine-console-reporter karma-jasmine karma-jasmine-html-reporter nyc --save-dev
npm install browserify browserify-istanbul istanbul watchify --save-dev
npm install eslint eslint-plugin-jasmine --save-dev

### Run test
npx karma start || npm test

### Check eslint
npx eslint src/*.js
