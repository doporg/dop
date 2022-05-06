FROM python:3.9.1

ADD . /code
WORKDIR /code
RUN pip3 install -r requirements.txt && \
    apt-get install git && \
    mkdir /code/src/defect_features/utils/data_tmp && \
    mkdir /code/src/defect_features/report && \
    mkdir /code/src/train

CMD ["python", "/code/src/app.py"]