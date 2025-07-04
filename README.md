# 📈 Zenvest

**Zenvest** is a modern Android application built using **Jetpack Compose** that allows users to explore stock market data, search for stocks, view detailed company overviews, and manage a personal watchlist. The app fetches real-time stock data using the **Alpha Vantage API**.

---

## ✨ Features

- 🔍 **Stock Search**: Search stocks by symbol or company name.
- 📄 **Company Overview**: View stock price, P/E ratio, market cap, 52-week range, and more.
- 📈 **Stock Chart**: Line chart displaying historical price trends.
- 🚀 **Top Movers**: Lists for top gainers, losers, and most active stocks.
- ⭐ **Watchlist**: Add/remove stocks with toggle button and local persistence using Room DB.
- 🌐 **Real-Time Data**: Live data integrated via [Alpha Vantage](https://www.alphavantage.co/).
- 🎨 **Modern UI**: Built with Jetpack Compose + Material 3 components.
- 🔀 **Navigation**: Jetpack Navigation Compose for smooth transitions.

---
<h3>📸 Screenshots</h3>

<p align="center">
  <img src="screenshots/topviewers.jpg" alt="Top Movers" width="170"/>
  <img src="screenshots/overview.jpg" alt="Overview" width="170"/>
  <img src="screenshots/pricechart.jpg" alt="Price Chart" width="170"/>
</p>

---

## 🧰 Tech Stack

| Layer            | Technology                          |
|------------------|--------------------------------------|
| **Language**     | Kotlin                               |
| **UI**           | Jetpack Compose + Material 3         |
| **Architecture** | MVVM + ViewModel + StateFlow         |
| **Navigation**   | Jetpack Navigation Compose           |
| **Networking**   | Retrofit + Gson                      |
| **Local Storage**| Room Database                        |
| **Image Loading**| Coil *(optional)*                    |
| **Logging**      | Timber *(optional)*                  |
| **API Provider** | [Alpha Vantage](https://www.alphavantage.co/) |

---

## 📁 Project Structure

```
zenvest/
├── app/                        # Main app module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/zenvest/
│   │   │   │   ├── api/             # API-related classes (Retrofit, data models)
│   │   │   │   ├── companyOverview/ # Company overview screen and ViewModel
│   │   │   │   ├── stockchart/      # Stock chart and search functionality
│   │   │   │   ├── topmovers/       # Top movers screen and ViewModel
│   │   │   │   ├── ui/              # Shared UI components
│   │   │   │   ├── watchlist/       # Watchlist feature with Room database
│   │   │   │   └── Routes.kt        # Navigation routes
│   │   ├── res/                     # Resources (colors, themes, etc.)
│   │   └── AndroidManifest.xml
├── screenshots/                    # Screenshots for README
└── README.md                       # Project documentation
```







