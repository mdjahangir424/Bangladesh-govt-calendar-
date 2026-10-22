# Bangladesh Government Calendar (বাংলাদেশ সরকারি ক্যালেন্ডার)

A native Android application providing a comprehensive Gregorian, Bengali (বঙ্গাব্দ), and Hijri (হিজরি) calendar with verified government holiday notifications for Bangladesh, referencing official circulars from the Ministry of Public Administration (MOPA).

---

## 1. App Owner & Creator Attribution

- **Creator / Developer:** Md Jahangir Alam
- **Contact Phone:** [+8801778600028](tel:+8801778600028)
- **Contact Email:** [mdjahangiralam2528@gmail.com](mailto:mdjahangiralam2528@gmail.com)

### Bengali Attribution:
> **অ্যাপটির নির্মাতা ও স্বত্বাধিকার দাবিদার:** Md Jahangir Alam  
> **যোগাযোগ:** +8801778600028  
> **ইমেইল:** mdjahangiralam2528@gmail.com  

### English Attribution & Rights:
> **Created by Md Jahangir Alam**  
> *Creator's ownership claim and copyright notice are subject to applicable law and third-party rights.*

### Copyright Notice:
> **© 2026 Md Jahangir Alam. All rights reserved, subject to applicable law and third-party rights.**

### Government Non-Affiliation Disclosure:
This application is an independently developed open-source utility by Md Jahangir Alam. It is **NOT** an official government application and does not claim official endorsement or affiliation with the Government of Bangladesh. All government holiday lists are compiled from publicly issued gazettes and circulars from the **Ministry of Public Administration (MOPA)** ([https://mopa.gov.bd/pages/public-holiday/](https://mopa.gov.bd/pages/public-holiday/)).

---

## 2. Key Features

1. **Triple Calendar Display:**
   - **Gregorian Date:** Formatted in both Bengali and English.
   - **Bengali Date (বঙ্গাব্দ):** Implements the official revised Bangladesh calendar (Bangla Academy 2019 standard) with correct month lengths, 6 seasonal periods, and leap-year Falgun adjustments.
   - **Hijri Date (হিজরি):** Based on the Umm al-Qura standard with a user-configurable -2 to +2 day offset to reflect local Bangladesh National Moon Sighting Committee announcements.

2. **Verified Bangladesh Government Holidays:**
   - Bundled verified holiday schedules for 2025 and 2026 from the Ministry of Public Administration (MOPA).
   - Clear classification into **General Public Holidays (সাধারণ ছুটি)**, **Executive Order Holidays (নির্বাহী আদেশ)**, and **National Observances (জাতীয় দিবস)**.
   - Visual distinction between weekly holidays (Friday & Saturday) and official gazetted holidays.
   - Safe in-app browser launcher opening the official MOPA source page.

3. **100% Offline Capability:**
   - No login, account creation, or internet connection required.
   - All converters and holiday schedules operate fully on-device.

4. **Bilingual & Modern Material 3 UI:**
   - Deep emerald green and white palette inspired by the Bangladesh flag with subtle crimson accents.
   - Instant language switch between Bengali (বাংলা) and English.
   - Light, Dark, and System theme support.

5. **Direct Developer Contact:**
   - Built-in tap-to-call and tap-to-email actions launching the device dialer and mail client.

---

## 3. Calendar Conventions & Accuracy

- **Bengali Calendar:**
  - Standard: Bangla Academy Revision (2019).
  - First 6 months (Boishakh, Jaishtho, Asharh, Srabon, Bhadro, Ashwin): 31 days each.
  - Next 5 months (Kartik, Ogrohayon, Poush, Magh, Choitro): 30 days each.
  - Falgun: 29 days (30 days during Gregorian leap years).
  - Pahela Baishakh: Fixed on April 14 every year.

- **Hijri Calendar:**
  - Standard astronomical calculations (Umm al-Qura) with ±2 day manual adjustment available in Settings.
  - All calculated Hijri dates are labeled as estimates subject to official moon-sighting confirmation.

- **Verification Status:**
  - **2025 Holidays:** Verified against MOPA gazette notifications.
  - **2026 Holidays:** Verified against MOPA annual holiday schedule.
  - **2027 & Future Years:** Flagged explicitly as *"Official holiday list not yet verified by government gazette"* until official notifications are released.

---

## 4. Build & Testing Instructions

### Prerequisites
- JDK 17 or higher
- Android SDK with API 36 support (Minimum SDK: 26)
- Gradle 8.11+ / AGP 9.1+

### Running Unit & Robolectric Tests:
```bash
gradle :app:testDebugUnitTest
```

### Compiling Debug APK:
```bash
gradle :app:assembleDebug
```
The APK will be generated at `app/build/outputs/apk/debug/app-debug.apk`.

### Generating a Signed Android App Bundle (AAB) for Google Play:
1. Ensure your upload keystore is configured or set environment variables:
   ```bash
   export KEYSTORE_PATH="/path/to/upload-keystore.jks"
   export STORE_PASSWORD="your-keystore-password"
   export KEY_PASSWORD="your-key-password"
   ```
2. Build the release bundle:
   ```bash
   gradle :app:bundleRelease
   ```
3. Locate the output bundle at `app/build/outputs/bundle/release/app-release.aab`.

---

## 5. Google Play Store Listing Materials

### Bengali Store Listing Draft
**Title (৩০ অক্ষর):**
বাংলাদেশ সরকারি ক্যালেন্ডার

**Short Description (৮০ অক্ষর):**
সরকারি ছুটির তালিকা, ইংরেজি, বাংলা ও হিজরি ক্যালেন্ডার - শতভাগ অফলাইন।

**Full Description:**
বাংলাদেশ সরকারি ক্যালেন্ডার একটি নির্ভরযোগ্য ও সম্পূর্ণ অফলাইন ক্যালেন্ডার অ্যাপ্লিকেশন।

বৈশিষ্ট্যসমূহ:
• জনপ্রশাসন মন্ত্রণালয় (MOPA) এর প্রজ্ঞাপন অনুযায়ী সরকারি সাধারণ ও নির্বাহী ছুটি।
• বাংলা একাডেমি ২০১৯ সংশোধিত নিয়ম অনুযায়ী সঠিক বাংলা তারিখ ও ঋতু।
• হিজরি ক্যালেন্ডার ও চাঁদ দেখার সমন্বয় (-২ থেকে +২ দিন)।
• শুক্রবার ও শনিবারের সাপ্তাহিক ছুটির পরিষ্কার নির্দেশক।
• আসন্ন ছুটির কাউন্টডাউন ও নোটিফিকেশন সুবিধা।
• সম্পূর্ণ অফলাইন - কোনো ইন্টারনেট বা লগইন প্রয়োজন নেই।
• শতভাগ বিজ্ঞাপনমুক্ত ও ব্যবহারকারী-বান্ধব ডিজাইন।

নির্মাতা ও স্বত্বাধিকার দাবিদার: Md Jahangir Alam
যোগাযোগ: +8801778600028
ইমেইল: mdjahangiralam2528@gmail.com
© 2026 Md Jahangir Alam. সর্বস্বত্ব সংরক্ষিত।

---

### English Store Listing Draft
**Title (30 chars max):**
BD Government Calendar

**Short Description (80 chars max):**
Bangladesh government holidays, Gregorian, Bangla and Hijri calendar. Offline.

**Full Description:**
Bangladesh Government Calendar is an independent, native Android calendar utility designed specifically for Bangladesh.

Key Features:
• Verified general and executive public holidays from the Ministry of Public Administration (MOPA).
• Accurate Bengali calendar adhering to the official Bangla Academy 2019 revision.
• Hijri Islamic lunar calendar with local moon-sighting adjustment (±2 days).
• Clear distinction for Friday and Saturday government weekends.
• Upcoming holiday countdown and optional morning reminders.
• 100% offline functionality with zero data collection or tracking.
• Modern Material 3 interface with Bengali and English language support.

Independent Developer: Md Jahangir Alam  
Contact: +8801778600028  
Email: mdjahangiralam2528@gmail.com  
© 2026 Md Jahangir Alam. All rights reserved.
