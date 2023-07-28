import pandas as pd
import datetime

data = pd.read_csv(r'D:/Coding/Android/StaySafe/data-analysis/Shooting_and_Firearm_Discharges_Open_Data.csv', index_col=False)

## Drop rows that have neighbourhood as NSA
data = data[data.NEIGHBOURHOOD_158 != "NSA"]

## Change OCC_DATE format
date_col = []
for index, row in data.iterrows():
    date = row['OCC_DATE'].split()[0]
    date_col.append(date)
data['DATE'] = date_col

## Select only the required columns
data = data[['X', 'Y', 'DATE', 'OCC_YEAR', 'OCC_MONTH', 'DEATH', 'INJURIES', 'NEIGHBOURHOOD_158']]

## Convert OCC_DATE to datetime format
data['DATE'] = pd.to_datetime(data['DATE'], format='%Y/%m/%d')

## Add a safety score metric
this_year = datetime.date.today().year
fatality_score = []
for index, row in data.iterrows():
  ## Factoring the fatality of the crime
  score = int(row['DEATH']) * 10 + int(row['INJURIES']) * 5

  ## Factoring the recency of the crime
  if (this_year - int(row['OCC_YEAR']) <= 5):
    score += 10

  fatality_score.append(score)

data['FATALITY_SCORE'] = fatality_score

## Group the data by NEIGHBOURHOOD_158, OCC_MONTH, OCC_DAY and OCC_TIME_RANGE
data = data[["NEIGHBOURHOOD_158", "OCC_MONTH", "FATALITY_SCORE"]]
data = data.groupby(["NEIGHBOURHOOD_158", "OCC_MONTH"], as_index=False)["FATALITY_SCORE"].sum()

## Create the Hood | Month | Rank table
## The logic for rank is as follows:
#### A rank will be given for each hood based on how high its fatality score is.
#### Rank 1: Indicates very low safety risk (0-31 lowest fatality score hoods)
#### Rank 2: Indicates low safety risk (31-63 ")
#### Rank 3: Indicates moderate safety risk (63-95 ")
#### Rank 4: Indicates high safety risk (95-126 ")
#### Rank 5: Indicates very high safety risk (126-158 ")
months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"]
hoods = data["NEIGHBOURHOOD_158"].unique()

month_wise_db = {}
for month in months:
  print(">>> Generating csv for the month of", month)
  temp = data[data["OCC_MONTH"] == month]

  max_fatality_score = float(temp["FATALITY_SCORE"].max())

  for hood in hoods:
    month_wise_db[hood] = 0

  for index, row in temp.iterrows():
    month_wise_db[row["NEIGHBOURHOOD_158"]] += row["FATALITY_SCORE"] / max_fatality_score
  
  temp = temp.sort_values(by=['FATALITY_SCORE'], ascending=True).tail(5)

  month_df = pd.DataFrame(list(zip(month_wise_db.keys(), month_wise_db.values())), columns =['NEIGHBOURHOOD_158', 'FATALITY_SCORE'])
  df_name = month + ".csv"
  csv_file = month_df.to_csv(df_name, index = False)

  month_wise_db.clear()
  
print(">>> Successfully generated csv files for all 12 months")

## Export to CSV format
csv_file = data.to_csv('neighbourhood_score_map.csv', index = True)

data.head()