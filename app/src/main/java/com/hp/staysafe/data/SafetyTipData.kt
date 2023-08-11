package com.hp.staysafe.data

public class safetyTipData() {
    companion object {
        private val safetyTipsList = listOf(
            "Safety Sniff: Avoid travelling to Glenfield-Jane Heights at this time, I sniff a high safety risk in the neighbourhood.",
            "Safety Sniff: Avoid travelling to Mount Olive-Silverstone-Jamestown, I sniff a high safety risk in the neighbourhood.",
            "Safety Sniff: Morning is the safest time of the day, so don’t shy away from taking me on your morning walks!",
            "Safety Sniff: Evenings have high safety risk, so please take extra precautions when returning late from work or school!",
            "Safety Sniff: February is the safest month of the year, afterall it is a month of love! Spread love and be kind!",
            "Safety Sniff: August and July have the highest safety risk amongst all months, I think the heat makes people go crazy!",
            "Safety Sniff: Forest Hill South is the safest neighbourhood in Toronto. I always love to play frisbee in the nearby Cedarvale Park!",
            "Safety Sniff: Kingsway South is one of the safest neighbourhoods in Toronto. I always enjoy a nice dip in the Lambton-Kingsway Park Pool!",
            "Safety Sniff: Toronto is much safer than many big cities of our southern neighbour. You should stay alert but not afraid!",
            "Safety Sniff: If you are a student, please enquire if your institution provides any campus police services.",
            "Safety Sniff: Watch your surroundings, when using an ATM. Prefer ATMs within a bank branch during daylight hours.",
            "Safety Sniff: Each TTC Subway station has an emergency phone, prefer standing close to it when travelling alone."
        )

        fun getSafetyTip(): String {
            var randomIndex = (0..11).random()
            return safetyTipsList[randomIndex]
        }
    }
}
