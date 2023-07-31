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

# Create dataframe of Neighbourhood - Avg(X) - Avg(Y)
neighbourhood_xy = data[["NEIGHBOURHOOD_158", "X", "Y"]]
neighbourhood_xy.sort_values(by=['NEIGHBOURHOOD_158'])
neighbourhood_xy = neighbourhood_xy.groupby(["NEIGHBOURHOOD_158"], as_index=False)[["X", "Y"]].mean()

# Export file to csv
csv_file = neighbourhood_xy.to_csv("neighbourhood_xy.csv", index = False)

## Group the data by NEIGHBOURHOOD_158, OCC_MONTH, OCC_DAY and OCC_TIME_RANGE
data = data[["NEIGHBOURHOOD_158", "OCC_MONTH", "FATALITY_SCORE"]]
data = data.groupby(["NEIGHBOURHOOD_158", "OCC_MONTH"], as_index=False)["FATALITY_SCORE"].sum()

## Create the Hood | Month | Rank table
## The logic for rank is as follows:
#### A rank will be given for each hood based on how high its fatality score (FS) is relative to the hood with the highest FS
#### To do so we divide each hood's FS for the month by the max observed FS during that month
#### A value between 0-0.2: Very low safety risk
#### A value between 0.2-0.4: Low safety risk
#### A value between 0.4-0.6: Moderate safety risk
#### A value between 0.6-0.8: High saftey risk
#### A value between 0.8-1.0: Very high safety risk

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