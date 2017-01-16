package com.matthewtamlin.responsum.example;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.matthewtamlin.responsum.library.answer.Answer;
import com.matthewtamlin.responsum.library.answer.ImmutableAnswer;
import com.matthewtamlin.responsum.library.answer_group.AnswerGroup;
import com.matthewtamlin.responsum.library.answer_group.SelectionLimitedAnswerGroup;
import com.matthewtamlin.responsum.library.answer_view.AlphaDecorator;
import com.matthewtamlin.responsum.library.answer_view.AlphaDecorator.AlphaSupplier;
import com.matthewtamlin.responsum.library.answer_view.ColorFadeDecorator;
import com.matthewtamlin.responsum.library.answer_view.ColorFadeDecorator.ColorSupplier;
import com.matthewtamlin.responsum.library.answer_view.DecoratedAnswerCard;

import java.util.LinkedHashMap;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity extends AppCompatActivity {
	private static final String QUESTION = "What is the answer to the Ultimate Question of Life, " +
			"the Universe, and Everything?";

	private final LinkedHashMap<CharSequence, Answer> answerMap = new LinkedHashMap<>();

	private TextView questionContainer;

	private AnswerGroup<DecoratedAnswerCard> answerGroup;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		populateAnswerMap();
		createUi();

		questionContainer.setText(QUESTION);

		for (final CharSequence identifier : answerMap.keySet()) {
			final DecoratedAnswerCard decoratedAnswerCard = new DecoratedAnswerCard(this);
			decoratedAnswerCard.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));

			decoratedAnswerCard.setIdentifier(identifier, false);
			decoratedAnswerCard.setAnswer(answerMap.get(identifier), false);
			decoratedAnswerCard.addDecorator(createColorFadeDecorator(), false);
			decoratedAnswerCard.addDecorator(createAlphaFadeDecorator(), false);

			answerGroup.addAnswer(decoratedAnswerCard);
		}
	}

	private void populateAnswerMap() {
		answerMap.put("A", new ImmutableAnswer("To live long and prosper.", false));
		answerMap.put("B", new ImmutableAnswer("To write really long sentences in a way which " +
				"causes the word count to be raised to an unnecessarily high value without " +
				"adding any additional/supplemental information or providing any value to the " +
				"reader.", false));
		answerMap.put("C", new ImmutableAnswer("To love and be loved.", false));
		answerMap.put("D", new ImmutableAnswer("No one knows the answer to this question.", true));
		answerMap.put("E", new ImmutableAnswer("To find the final digit of Pi.", false));
		answerMap.put("F", new ImmutableAnswer("To propagate one's species.", false));
	}

	private void createUi() {
		questionContainer = new TextView(this);
		questionContainer.setTextSize(20);
		questionContainer.setGravity(Gravity.CENTER);

		answerGroup = new SelectionLimitedAnswerGroup<>(this);

		final LinearLayout rootViewGroup = new LinearLayout(this);
		rootViewGroup.setOrientation(LinearLayout.VERTICAL);
		rootViewGroup.addView(questionContainer);
		rootViewGroup.addView((View) answerGroup);

		setContentView(rootViewGroup, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
	}

	private ColorFadeDecorator createColorFadeDecorator() {
		final ColorSupplier colorSupplier = new ColorSupplier() {
			@Override
			public int getColor(final boolean marked, final boolean selected,
					final boolean answerIsCorrect) {
				if (marked) {
					if (selected) {
						return answerIsCorrect ? Color.GREEN : Color.RED;
					} else {
						return answerIsCorrect ? Color.RED : Color.GREEN;
					}
				} else {
					return selected ? Color.BLUE : Color.WHITE;
				}
			}
		};

		return new ColorFadeDecorator(colorSupplier);
	}

	private AlphaDecorator createAlphaFadeDecorator() {
		final AlphaSupplier alphaSupplier = new AlphaSupplier() {
			@Override
			public float getAlpha(final boolean marked, final boolean selected,
					final boolean answerIsCorrect) {
				if (marked && !selected && !answerIsCorrect) {
					return 0.5f;
				} else {
					return 1f;
				}
			}
		};

		return new AlphaDecorator(alphaSupplier);
	}
}