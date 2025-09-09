import matplotlib
import numpy as np
import pandas as pd
import seaborn as sns
import sklearn
import tensorflow as tf
from IPython.core.display_functions import display
from keras.callbacks import History
from matplotlib import pyplot as plt
from sklearn.model_selection import train_test_split
from tensorflow.keras import layers

from src.utils.env import get_data_dir


def load_dataframe():
    data_directory = get_data_dir()

    df = pd.read_csv(f"{data_directory}/moleculenet/delaney-processed.csv")
    display(df.info())
    display(df.head())
    return df


def show_data_correlation(df: pd.DataFrame):
    sns.pairplot(
        df[
            [
                "ESOL predicted log solubility in mols per litre",
                "Minimum Degree",
                "Molecular Weight",
                "Number of H-Bond Donors",
                "Number of Rings",
                "Number of Rotatable Bonds",
                "Polar Surface Area",
                "measured log solubility in mols per litre",
            ]
        ],
        hue="ESOL predicted log solubility in mols per litre",
        diag_kind="kde",
    )


def plot_loss(history: History):
    plt.plot(history.history["loss"], label="loss")
    plt.plot(history.history["val_loss"], label="val_loss")
    plt.ylim([0, 10])
    plt.xlabel("Epoch")
    plt.ylabel("Error")
    plt.legend()
    plt.grid(True)
    plt.show()


def plot_predictions(test_labels: pd.Series, predicted_labels: np.ndarray):
    plt.scatter(test_labels, predicted_labels, label="Predictions")
    plt.xlabel("True values")
    plt.ylabel("Predicted values")
    plt.legend()
    plt.grid(True)
    plt.show()


def split_data(features: pd.Series, labels: pd.Series):
    return train_test_split(features, labels, train_size=0.75, random_state=0)


def train_model(train_features, train_labels) -> tuple[tf.keras.Sequential, History]:
    normalizer = layers.Normalization(
        input_shape=[
            1,
        ],
        axis=None,
    )
    normalizer.adapt(np.array(train_features))

    model = tf.keras.Sequential([normalizer, layers.Dense(units=1)])
    display(model.summary())

    model.compile(optimizer=tf.keras.optimizers.Adam(learning_rate=0.1), loss=tf.keras.losses.MeanSquaredError())

    history = model.fit(train_features, train_labels, epochs=100, verbose=0, validation_split=0.2)
    return model, history


def main():
    print(tf.__version__)
    print(sklearn.__version__)

    tf.random.set_seed(22)

    matplotlib.rcParams["figure.figsize"] = [9, 6]

    df = load_dataframe()
    show_data_correlation(df)

    features = df["Molecular Weight"]
    display(features.head())
    labels = df["ESOL predicted log solubility in mols per litre"]

    train_features, test_features, train_labels, test_labels = split_data(features, labels)
    display(train_features.describe())

    model, history = train_model(train_features, train_labels)
    plot_loss(history)

    test_loss: float = model.evaluate(test_features, test_labels, verbose=0)
    print(f"Test loss: {test_loss}")

    molecular_weights: tf.Tensor = tf.linspace(0.0, 282, 282)
    predicted_solubility: np.ndarray = model.predict(molecular_weights)
    plot_predictions(test_labels, predicted_solubility)


if __name__ == "__main__":
    main()
