# pylint: disable=import-error
import pandas as pd

_FILE_PATH = "../../../../data/foodb/foodb_2020_04_07_csv/Food.csv"

def read_food_data():
    return pd.read_csv(_FILE_PATH)

def _drop_columns(food_df: pd.DataFrame):
    return food_df.drop(columns=[
        "id", "public_id",
        "legacy_id", "picture_file_name", "picture_content_type", "picture_file_size",
        "picture_updated_at", "itis_id", "creator_id", "updater_id", "ncbi_taxonomy_id",
        "created_at", "updated_at", "export_to_foodb", "export_to_afcdb",
        "name_scientific", "wikipedia_id", "food_group", "food_subgroup"])

def prepare_dataset(food_df: pd.DataFrame):
    food_df = _drop_columns(food_df)

    columns = ["description"]
    food_df[columns] = food_df[columns].fillna(value="data is not specified")
    columns = ["category"]
    food_df[columns] = food_df[columns].fillna(value="unknown")

    x = food_df.drop(columns=["food_type"])
    y = food_df[["food_type"]]

    return food_df, x, y
