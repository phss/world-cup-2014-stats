(use '[clojure.string :only (split-lines split capitalize)])

(defn read-tabular-file [filename]
  (let [data (slurp filename)
        lines (split-lines data)
        split-by-tab #(split % #"\t")]
    (map split-by-tab lines)))

(defn nameify [s]
  (capitalize s))

(defn convert-player [country data]
  (let [shirt (data 0)]
    {
      :id (str country "_" shirt)
      :country (nameify country)
      :name (data 1)
      :shirt_number shirt
      :date_of_birth (data 2)
      :position (data 3)
      :club (data 4)
      :height (data 5)
    }))

(def raw (read-tabular-file "2010/brazil.data"))

(println (convert-player "brazil" (first raw)))
