import matplotlib.pyplot as plt
import numpy as np


def sigmoid(x):
    # 直接返回sigmoid函数
    return 1. / (1. + np.exp(-x))


def plot_sigmoid():
    # param:起点，终点，间距
    x = np.arange(-8, 8, 0.2)
    y = sigmoid(x)
    plt.plot(x, y)
    plt.show()


if __name__ == '__main__':
    plot_sigmoid()