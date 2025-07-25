package pl.myc22ka.mathapp.utils.managers.files.records;

import pl.myc22ka.mathapp.model.set.ISet;

public record SetRecord(
        ISet A,
        ISet B,
        ISet union,
        ISet intersection,
        ISet difference) {

}
