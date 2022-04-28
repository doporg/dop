FROM python:3.9.1

ADD . /code
WORKDIR /code
RUN pip3 install -r requirements.txt && \
    apt-get install git

CMD ["python", "/code/src/app.py"]