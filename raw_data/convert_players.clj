(use '[clojure.string :only (split-lines split join capitalize)])
(require '[clojure.data.json :as json])

; Conversions
(def position { "GK" "Goalkeeper", "DF" "Defender", "MF" "Midfielder", "FW" "Forward" })

(def country-by-code {
  "ITA" "Italy"                      
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

; Reading from 
(defn read-tabular-file [filename]
  (let [data (slurp filename)
        lines (split-lines data)
        split-by-tab #(split % #"\t")]
    (map split-by-tab lines)))


(def raw (read-tabular-file "2010/brazil.data"))

(def brazil (map #(convert-player "brazil" %) raw))

(println (json/write-str brazil))

