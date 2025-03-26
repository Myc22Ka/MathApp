package pl.myc22ka.mathapp.model;

import org.matheclipse.core.interfaces.IExpr;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Point {
    private IExpr x;
    private IExpr y;
}
