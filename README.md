# ⛵ Boat Fuel Tracker

A Progressive Web App (PWA) for tracking boat fuel consumption and expenses with Firebase authentication and cloud storage.

## 🌟 Features

- **User Authentication** - Email/password and Google Sign-In via Firebase
- **Cloud Data Storage** - Fuel-up records stored in Firestore Database
- **User Isolation** - Each user's data is private and secure
- **Admin Features** - Admin users can export data to CSV and clear all records
- **Progressive Web App** - Install on mobile devices for offline access
- **Responsive Design** - Works on desktop, tablet, and mobile
- **Real-time Sync** - Data syncs across all your devices

## 📊 Track Your Fuel Data

- Date of fill-up
- Gallons purchased
- Price per gallon
- Total cost (calculated automatically)
- Engine hours (optional)
- Location (optional)
- Notes (optional)

## 🚀 Live Demo

**Website:** https://tsanders-rh.github.io/boat-fuel-tracker/

## 📱 Installation

### On iOS (Safari)
1. Open the website in Safari
2. Tap the Share button (square with arrow)
3. Tap "Add to Home Screen"
4. Tap "Add"

### On Android (Chrome)
1. Open the website in Chrome
2. Tap the menu (three dots)
3. Tap "Add to Home Screen"
4. Tap "Add"

## 🛠️ Technology Stack

- **Frontend:** HTML5, CSS3, JavaScript (Vanilla)
- **Authentication:** Firebase Authentication
- **Database:** Cloud Firestore
- **Hosting:** GitHub Pages
- **PWA Features:** Service Worker, Web App Manifest

## 🔧 Firebase Setup

This application requires Firebase configuration. Follow these steps:

1. **Create a Firebase Project**
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create a new project or use existing one

2. **Enable Authentication**
   - Email/Password authentication
   - Google Sign-In

3. **Create Firestore Database**
   - Set up Cloud Firestore
   - Configure security rules (see `FIREBASE_SETUP.md`)

4. **Get Your Configuration**
   - Go to Project Settings
   - Copy your Firebase config
   - Update `index.html` with your config (around line 595)

See [FIREBASE_SETUP.md](./FIREBASE_SETUP.md) for detailed setup instructions.

## 👤 User Roles

### Regular Users
- Track their own fuel-up records
- View statistics (total fill-ups, gallons, spending)
- Add, view, and delete their own records

### Admin Users
- All regular user features
- **Export to CSV** - Export all fuel data
- **Clear All Data** - Delete all fuel-up records

To make a user an admin, add `isAdmin: true` field to their user document in Firestore.

## 📈 Statistics Tracked

- **Total Fill-Ups** - Number of fuel purchases
- **Total Gallons** - Total fuel purchased
- **Total Spent** - Total money spent on fuel
- **Average Price/Gallon** - Average fuel price

## 🔒 Security

- **Authentication Required** - Must sign in to use the app
- **User Data Isolation** - Users can only access their own data
- **Firestore Security Rules** - Server-side security enforcement
- **HTTPS Only** - Encrypted communication

## 🏗️ Project Structure

```
boat-fuel-tracker/
├── index.html              # Main application
├── manifest.json           # PWA manifest
├── service-worker.js       # Service worker for offline support
├── FIREBASE_SETUP.md       # Firebase configuration guide
└── README.md              # This file
```

## 🌐 Local Development

1. Clone the repository:
   ```bash
   git clone https://github.com/tsanders-rh/boat-fuel-tracker.git
   cd boat-fuel-tracker
   ```

2. Update Firebase configuration in `index.html`

3. Serve the files locally:
   ```bash
   # Using Python 3
   python3 -m http.server 8000

   # Or using Node.js
   npx http-server
   ```

4. Open http://localhost:8000 in your browser

## 📝 Adding a Fuel-Up

1. Sign in with your account
2. Fill in the "Add Fuel-Up" form:
   - Date (required)
   - Gallons (required)
   - Price per Gallon (required)
   - Engine Hours (optional)
   - Location (optional)
   - Notes (optional)
3. Click "Add Fuel-Up"
4. Your record appears in the history table

## 💾 Data Storage

All data is stored in Google Cloud Firestore with the following structure:

```
users/{userId}/
  ├── email
  ├── displayName
  ├── isAdmin
  ├── createdAt
  └── fuelUps/{fuelUpId}/
      ├── date
      ├── gallons
      ├── pricePerGallon
      ├── totalCost
      ├── engineHours
      ├── location
      ├── notes
      └── createdAt
```

## 🔄 Legacy J2EE Version

A legacy J2EE version of this application exists on the `j2ee-conversion` branch for testing Konveyor application modernization tools. It contains intentional anti-patterns and deprecated APIs.

```bash
git checkout j2ee-conversion
```

See [README_J2EE.md](https://github.com/tsanders-rh/boat-fuel-tracker/blob/j2ee-conversion/README_J2EE.md) on that branch for details.

## 🤝 Contributing

This is a personal project, but suggestions and improvements are welcome!

## 📄 License

MIT License

## 🙏 Acknowledgments

- Firebase for authentication and database services
- GitHub Pages for hosting
- Claude Code for development assistance

## 📧 Contact

Questions or issues? Open an issue on GitHub.

---

Made with ❤️ for boat owners who want to track their fuel consumption.
