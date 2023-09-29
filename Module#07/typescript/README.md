## Task Typescript

### Build, run, test

#### For Europe Company project:
npm run build:europe
npm run run:europe
npm run test:europe

#### For American Company project:
npm run build:america
npm run run:america
npm run test:america

#### For British Company project:
npm run build:british
npm run run:british
npm run test:british

### Check eslint
cd EuropeCompany && npx eslint *.ts
cd BritishCompany && npx eslint *.ts
cd AmericanCompany && npx eslint *.ts

npx jest British.test.ts
npx jest

### Init
npm install -g typescript
npm install -g ts-node

mkdir typescript
cd typescript
npm init
npm i eslint @typescript-eslint/eslint-plugin @typescript-eslint/parser --save-dev
npm i --save-dev jest @types/jest
npm i node-localstorage

### Create Europe Company project
mkdir EuropeCompany
cd EuropeCompany
touch index.html
touch app.ts
touch tsconfig.json
echo "<script src='app.js'></script>" >> index.html
npm init

### Create American Company project
mkdir AmericanCompany
cd AmericanCompany
touch index.html
touch app.ts
touch tsconfig.json
echo "<script src='app.js'></script>" >> index.html
npm init

### Create British Company project
mkdir BritishCompany
cd BritishCompany
touch index.html
touch app.ts
touch tsconfig.json
echo "<script src='app.js'></script>" >> index.html
npm init
