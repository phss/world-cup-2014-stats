(use '[clojure.string :only (split-lines split)])

(defn read_tabular_file [filename]
  (let [data (slurp filename)
        lines (split-lines data)
        split-by-tab #(split % #"\t")]
    (map split-by-tab lines)))


(def raw (read_tabular_file "2010/brazil.data"))

(println (ffirst raw))
