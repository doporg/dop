FROM  node:8-slim

RUN mkdir -p /home/Service

WORKDIR /home/Service

COPY . /home/Service

RUN npm install

EXPOSE 3000

CMD npm start
