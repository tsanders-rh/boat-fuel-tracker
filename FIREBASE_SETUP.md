# Firebase Setup Instructions

Follow these steps to configure Firebase for your Boat Fuel Tracker application.

## Step 1: Create a Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add project" or "Create a project"
3. Enter a project name (e.g., "Boat Fuel Tracker")
4. Accept the terms and click "Continue"
5. (Optional) Enable Google Analytics
6. Click "Create project"

## Step 2: Register Your Web App

1. In your Firebase project dashboard, click the **Web icon** (`</>`) to add a web app
2. Register your app with a nickname (e.g., "Boat Fuel Tracker Web")
3. Check "Also set up Firebase Hosting" if you want to use Firebase Hosting instead of GitHub Pages
4. Click "Register app"
5. Copy the Firebase configuration object shown

## Step 3: Update Your Application Config

1. Open `index.html` in your project
2. Find the Firebase configuration section (around line 596):
   ```javascript
   const firebaseConfig = {
       apiKey: "YOUR_API_KEY",
       authDomain: "YOUR_PROJECT_ID.firebaseapp.com",
       projectId: "YOUR_PROJECT_ID",
       storageBucket: "YOUR_PROJECT_ID.appspot.com",
       messagingSenderId: "YOUR_MESSAGING_SENDER_ID",
       appId: "YOUR_APP_ID"
   };
   ```
3. Replace the placeholder values with your actual Firebase config values

## Step 4: Enable Authentication

1. In the Firebase Console, go to **Build** → **Authentication**
2. Click "Get started"
3. Enable **Email/Password** authentication:
   - Click on "Email/Password"
   - Toggle "Enable"
   - Click "Save"
4. Enable **Google** authentication:
   - Click on "Google"
   - Toggle "Enable"
   - Select a support email
   - Click "Save"

## Step 5: Set Up Firestore Database

1. In the Firebase Console, go to **Build** → **Firestore Database**
2. Click "Create database"
3. Choose a location (select one close to your users)
4. Start in **Production mode** and click "Next"
5. Click "Enable"

## Step 6: Configure Firestore Security Rules

1. In Firestore Database, go to the **Rules** tab
2. Replace the default rules with:
   ```javascript
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /users/{userId} {
         // Allow users to read and write only their own data
         allow read, write: if request.auth != null && request.auth.uid == userId;

         match /fuelUps/{fuelUpId} {
           // Allow users to read and write only their own fuel-up records
           allow read, write: if request.auth != null && request.auth.uid == userId;
         }
       }
     }
   }
   ```
3. Click "Publish"

## Step 7: Add Authorized Domains

1. In Firebase Console, go to **Build** → **Authentication** → **Settings** tab
2. Scroll down to **Authorized domains**
3. Add your GitHub Pages domain:
   - Format: `YOUR_USERNAME.github.io`
   - Example: `tsanders-rh.github.io`
4. Click "Add domain"

## Step 8: Test Your Application

1. Commit and push your changes to GitHub:
   ```bash
   git add .
   git commit -m "Add Firebase authentication"
   git push
   ```
2. Open your GitHub Pages site
3. Try signing up with email/password
4. Try signing in with Google
5. Add some fuel-up records
6. Verify they appear in Firestore Database in the Firebase Console

## Troubleshooting

### "auth/unauthorized-domain" Error
- Make sure you added your GitHub Pages domain to Authorized domains in Firebase Authentication settings

### "Missing or insufficient permissions" Error
- Check your Firestore security rules
- Make sure you're signed in
- Verify the rules allow access to `/users/{userId}/fuelUps/`

### Google Sign-In Not Working
- Verify Google authentication is enabled in Firebase Console
- Check that you selected a support email
- Make sure pop-ups are not blocked in your browser

### Data Not Saving
- Open browser console (F12) to check for errors
- Verify Firestore is enabled
- Check that security rules are published

## Optional: Firebase Hosting

If you prefer Firebase Hosting over GitHub Pages:

1. Install Firebase CLI: `npm install -g firebase-tools`
2. Login: `firebase login`
3. Initialize: `firebase init`
   - Select "Hosting"
   - Choose your Firebase project
   - Set public directory to `.` (current directory)
   - Configure as single-page app: Yes
   - Set up automatic builds: No
4. Deploy: `firebase deploy`

## Next Steps

- **Data Migration**: If you have existing data in localStorage, users can export it as CSV from the old version and manually re-import
- **Email Verification**: Consider adding email verification for better security
- **Password Reset**: Implement password reset functionality using `auth.sendPasswordResetEmail()`
- **Profile Management**: Add user profile editing features

## Support

For more information, visit:
- [Firebase Documentation](https://firebase.google.com/docs)
- [Firebase Authentication](https://firebase.google.com/docs/auth)
- [Cloud Firestore](https://firebase.google.com/docs/firestore)
