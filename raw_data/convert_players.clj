(use '[clojure.string :only (split-lines split join capitalize)])
(require '[clojure.data.json :as json])

; Conversions
(def position { "GK" "Goalkeeper", "DF" "Defender", "MF" "Midfielder", "FW" "Forward" })

(def country-by-code {
  "ALG" "Algeria", "ARG" "Argentina", "AUS" "Australia", "AUT" "Austria", "BEL" "Belgium", "BRA" "Brazil", "BUL" "Bulgaria", "CIV" "Ivory Coast", "CHI" "Chile", "CHN" "China", "CMR" "Cameroon", "COL" "Colombia", "CYP" "Cyprus", "CZE" "Czech Republic" "DEN" "Denmark", "ECU" "Ecuador", "EGY" "Egypt", "ENG" "England", "ESP" "Spain", "FRA" "France", "GER" "Germany", "GHA" "Ghana", "GRE" "Greece", "HON" "Honduras", "ISR" "Israel", "ITA" "Italy", "JPN" "Japan", "KOR" "South Korea", "KSA" "Kazakhstan", "MEX" "Mexico", "NED" "Netherlands", "NOR" "Norway", "NZL" "New Zeland", "PAR" "Paraguay", "POL" "Poland", "POR" "Portugal", "PRK" "North Korea", "QAT" "Qatar", "ROU" "Romania", "RSA" "South Africa", "RUS" "Russia", "SCO" "Scotland", "SUI" "Switzerland", "SRB" "Serbia", "SVK" "Slovakia", "SVN" "Slovenia", "SWE" "Sweden", "TUR" "Turkey", "UAE" "United Emirates", "UKR" "Ukraine", "UNK" "Unknown", "URU" "Uruguay", "USA" "United States"                      
})

(def number read-string)

(defn nameify [s]
  (let [words (split s #" ")
        word-names (map capitalize words)]
    (join " " word-names)))

(defn iso-date [s]
  (let [[day month year] (split s #"/")]
    (format "%s-%s-%s" year month day)))

(defn club [s]
  (let [[_ club country-code] (re-find #"(\w+) \((\w+)\)" s)
        country (country-by-code country-code)]
    (if (nil? country)
      (throw (Exception. (str "No country for " s)))
      { :name club, :country country })))

; Reading from 
(defn read-tabular-file [filename]
  (let [data (slurp filename)
        lines (split-lines data)
        split-by-tab #(split % #"\t")]
    (map split-by-tab lines)))

; Player conversion
(defn convert-player [country data]
  (let [shirt (number (data 0))]
    {
      :id (str country "_" shirt)
      :country (nameify country)
      :name (nameify (data 1))
      :shirt_number shirt
      :date_of_birth (iso-date (data 2))
      :position (position (data 3))
      :club (club (data 4))
      :height (number (data 5))
    }))

(defn convert-country [file]
  (let [filename (. file getName)
        country (second (re-find #"(\w+)." filename)) 
        raw (read-tabular-file file)]
    (map #(convert-player country %) raw)))



(def raw-dir (clojure.java.io/file "2010/"))
(def raw-country-files (rest (file-seq raw-dir)))

(doseq [country-file raw-country-files]
  (println (convert-country country-file)))

