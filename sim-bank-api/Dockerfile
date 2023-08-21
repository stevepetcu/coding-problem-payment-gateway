FROM node:18

# See https://nodejs.org/en/docs/guides/nodejs-docker-webapp

# Create app directory
WORKDIR /usr/src/app

# Install app dependencies
# A wildcard is used to ensure both package.json AND package-lock.json are copied
# where available (npm@5+)
COPY package*.json ./

RUN npm install
# If you are building your code for production (but we don't have a package-lock.json since we're using pnpm)
# RUN npm ci --omit=dev

# Bundle app source
COPY . .

RUN npm run build

EXPOSE 5050
CMD [ "node", "./dist/server.js" ]
