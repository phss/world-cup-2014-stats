(use '[clojure.string :only (split-lines split)])

(defn read-tabular-file [filename]
  (let [data (slurp filename)
        lines (split-lines data)
        split-by-tab #(split % #"\t")]
    (map split-by-tab lines)))


(def raw (read-tabular-file "2010/brazil.data"))

(println (ffirst raw))
