(use '[clojure.string :only (split-lines split join capitalize)])
(require '[clojure.data.json :as json])

; Conversions
(def position { "GK" "Goalkeeper", "DF" "Defender", "MF" "Midfielder", "FW" "Forward" })

(def country-by-code {
  "ALG" "Algeria", "BRA" "Brazil", "BUL" "Bulgaria", "ENG" "England", "ESP" "Spain", "FRA" "France", "GER" "Germany", "GRE" "Greece", "ITA" "Italy", "POR" "Portugal", "SCO" "Scotland", "TUR" "Turkey"                      
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
      (throw (Exception. (str "No country for " country-code)))
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


(def raw (read-tabular-file "2010/brazil.data"))

(def brazil (map #(convert-player "brazil" %) raw))

;(println (json/write-str brazil))

(def directory (clojure.java.io/file "2010/"))
(def files (rest (file-seq directory)))
(def file (first files))
(def filename (. file getName))

(println (convert-country file))
